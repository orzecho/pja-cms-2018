import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWord } from 'app/shared/model/word.model';
import { WordService } from './word.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';

@Component({
    selector: 'jhi-word-update',
    templateUrl: './word-update.component.html'
})
export class WordUpdateComponent implements OnInit {
    private _word: IWord;
    isSaving: boolean;

    tags: ITag[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private wordService: WordService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ word }) => {
            this.word = word;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.word.id !== undefined) {
            this.subscribeToSaveResponse(this.wordService.update(this.word));
        } else {
            this.subscribeToSaveResponse(this.wordService.create(this.word));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWord>>) {
        result.subscribe((res: HttpResponse<IWord>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackTagById(index: number, item: ITag) {
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
    get word() {
        return this._word;
    }

    set word(word: IWord) {
        this._word = word;
    }
}
