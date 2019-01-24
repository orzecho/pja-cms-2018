import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrueFalseAnswer } from 'app/shared/model/true-false-answer.model';

@Component({
    selector: 'jhi-true-false-answer-detail',
    templateUrl: './true-false-answer-detail.component.html'
})
export class TrueFalseAnswerDetailComponent implements OnInit {
    trueFalseAnswer: ITrueFalseAnswer;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ trueFalseAnswer }) => {
            this.trueFalseAnswer = trueFalseAnswer;
        });
    }

    previousState() {
        window.history.back();
    }
}
