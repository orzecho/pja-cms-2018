import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWordsTest } from 'app/shared/model/words-test.model';
import { WordsTestService } from './words-test.service';
import { IUser, UserService } from 'app/core';
import { IWord } from 'app/shared/model/word.model';
import { WordService } from 'app/entities/word';

@Component({
    selector: 'jhi-words-test-update',
    templateUrl: './words-test-update.component.html'
})
export class WordsTestUpdateComponent implements OnInit {
    private _wordsTest: IWordsTest;
    isSaving: boolean;

    users: IUser[];

    words: IWord[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private wordsTestService: WordsTestService,
        private userService: UserService,
        private wordService: WordService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ wordsTest }) => {
            this.wordsTest = wordsTest;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.wordService.query().subscribe(
            (res: HttpResponse<IWord[]>) => {
                this.words = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.wordsTest.id !== undefined) {
            this.subscribeToSaveResponse(this.wordsTestService.update(this.wordsTest));
        } else {
            this.subscribeToSaveResponse(this.wordsTestService.create(this.wordsTest));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWordsTest>>) {
        result.subscribe((res: HttpResponse<IWordsTest>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackWordById(index: number, item: IWord) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get wordsTest() {
        return this._wordsTest;
    }

    set wordsTest(wordsTest: IWordsTest) {
        this._wordsTest = wordsTest;
    }
}
