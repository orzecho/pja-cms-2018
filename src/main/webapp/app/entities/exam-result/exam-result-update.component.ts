import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IExamResult } from 'app/shared/model/exam-result.model';
import { ExamResultService } from './exam-result.service';
import { IUser, UserService } from 'app/core';
import { IExam } from 'app/shared/model/exam.model';
import { ExamService } from 'app/entities/exam';

@Component({
    selector: 'jhi-exam-result-update',
    templateUrl: './exam-result-update.component.html'
})
export class ExamResultUpdateComponent implements OnInit {
    private _examResult: IExamResult;
    isSaving: boolean;

    users: IUser[];

    exams: IExam[];
    date: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private examResultService: ExamResultService,
        private userService: UserService,
        private examService: ExamService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ examResult }) => {
            this.examResult = examResult;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.examService.query().subscribe(
            (res: HttpResponse<IExam[]>) => {
                this.exams = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.examResult.date = moment(this.date, DATE_TIME_FORMAT);
        if (this.examResult.id !== undefined) {
            this.subscribeToSaveResponse(this.examResultService.update(this.examResult));
        } else {
            this.subscribeToSaveResponse(this.examResultService.create(this.examResult));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IExamResult>>) {
        result.subscribe((res: HttpResponse<IExamResult>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackExamById(index: number, item: IExam) {
        return item.id;
    }
    get examResult() {
        return this._examResult;
    }

    set examResult(examResult: IExamResult) {
        this._examResult = examResult;
        this.date = moment(examResult.date).format(DATE_TIME_FORMAT);
    }
}
