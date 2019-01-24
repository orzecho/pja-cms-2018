import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWrittenAnswer } from 'app/shared/model/written-answer.model';
import { WrittenAnswerService } from './written-answer.service';
import { IWord } from 'app/shared/model/word.model';
import { WordService } from 'app/entities/word';
import { IExamResult } from 'app/shared/model/exam-result.model';
import { ExamResultService } from 'app/entities/exam-result';

@Component({
    selector: 'jhi-written-answer-update',
    templateUrl: './written-answer-update.component.html'
})
export class WrittenAnswerUpdateComponent implements OnInit {
    private _writtenAnswer: IWrittenAnswer;
    isSaving: boolean;

    words: IWord[];

    examresults: IExamResult[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private writtenAnswerService: WrittenAnswerService,
        private wordService: WordService,
        private examResultService: ExamResultService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ writtenAnswer }) => {
            this.writtenAnswer = writtenAnswer;
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
        if (this.writtenAnswer.id !== undefined) {
            this.subscribeToSaveResponse(this.writtenAnswerService.update(this.writtenAnswer));
        } else {
            this.subscribeToSaveResponse(this.writtenAnswerService.create(this.writtenAnswer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWrittenAnswer>>) {
        result.subscribe((res: HttpResponse<IWrittenAnswer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
    get writtenAnswer() {
        return this._writtenAnswer;
    }

    set writtenAnswer(writtenAnswer: IWrittenAnswer) {
        this._writtenAnswer = writtenAnswer;
    }
}
