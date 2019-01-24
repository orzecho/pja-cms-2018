import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWrittenAnswer } from 'app/shared/model/written-answer.model';

@Component({
    selector: 'jhi-written-answer-detail',
    templateUrl: './written-answer-detail.component.html'
})
export class WrittenAnswerDetailComponent implements OnInit {
    writtenAnswer: IWrittenAnswer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ writtenAnswer }) => {
            this.writtenAnswer = writtenAnswer;
        });
    }

    previousState() {
        window.history.back();
    }
}
