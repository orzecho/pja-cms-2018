import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ITrueFalseAnswer } from 'app/shared/model/true-false-answer.model';
import { TrueFalseAnswerService } from './true-false-answer.service';
import { IWord } from 'app/shared/model/word.model';
import { WordService } from 'app/entities/word';
import { IExamResult } from 'app/shared/model/exam-result.model';
import { ExamResultService } from 'app/entities/exam-result';

@Component({
    selector: 'jhi-true-false-answer-update',
    templateUrl: './true-false-answer-update.component.html'
})
export class TrueFalseAnswerUpdateComponent implements OnInit {
    private _trueFalseAnswer: ITrueFalseAnswer;
    isSaving: boolean;

    words: IWord[];

    examresults: IExamResult[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private trueFalseAnswerService: TrueFalseAnswerService,
        private wordService: WordService,
        private examResultService: ExamResultService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ trueFalseAnswer }) => {
            this.trueFalseAnswer = trueFalseAnswer;
        });
        this.wordService.query().subscribe(
            (res: HttpResponse<IWord[]>) => {
                this.words = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.examResultService.query().subscribe(
            (res: HttpResponse<IExamResult[]>) => {
                this.examresults = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.trueFalseAnswer.id !== undefined) {
            this.subscribeToSaveResponse(this.trueFalseAnswerService.update(this.trueFalseAnswer));
        } else {
            this.subscribeToSaveResponse(this.trueFalseAnswerService.create(this.trueFalseAnswer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITrueFalseAnswer>>) {
        result.subscribe((res: HttpResponse<ITrueFalseAnswer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackWordById(index: number, item: IWord) {
        return item.id;
    }

    trackExamResultById(index: number, item: IExamResult) {
        return item.id;
    }
    get trueFalseAnswer() {
        return this._trueFalseAnswer;
    }

    set trueFalseAnswer(trueFalseAnswer: ITrueFalseAnswer) {
        this._trueFalseAnswer = trueFalseAnswer;
    }
}
