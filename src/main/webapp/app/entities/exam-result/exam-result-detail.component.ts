import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExamResult } from 'app/shared/model/exam-result.model';

@Component({
    selector: 'jhi-exam-result-detail',
    templateUrl: './exam-result-detail.component.html'
})
export class ExamResultDetailComponent implements OnInit {
    examResult: IExamResult;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ examResult }) => {
            this.examResult = examResult;
        });
    }

    previousState() {
        window.history.back();
    }
}
