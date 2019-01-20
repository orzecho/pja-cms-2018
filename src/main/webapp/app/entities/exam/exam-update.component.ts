import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IExam } from 'app/shared/model/exam.model';
import { ExamService } from './exam.service';
import { IUser, UserService } from 'app/core';
import { IWord } from 'app/shared/model/word.model';
import { WordService } from 'app/entities/word';

@Component({
    selector: 'jhi-exam-update',
    templateUrl: './exam-update.component.html'
})
export class ExamUpdateComponent implements OnInit {
    private _exam: IExam;
    isSaving: boolean;

    users: IUser[];

    words: IWord[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private examService: ExamService,
        private userService: UserService,
        private wordService: WordService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ exam }) => {
            this.exam = exam;
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
        if (this.exam.id !== undefined) {
            this.subscribeToSaveResponse(this.examService.update(this.exam));
        } else {
            this.subscribeToSaveResponse(this.examService.create(this.exam));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IExam>>) {
        result.subscribe((res: HttpResponse<IExam>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get exam() {
        return this._exam;
    }

    set exam(exam: IExam) {
        this._exam = exam;
    }
}
