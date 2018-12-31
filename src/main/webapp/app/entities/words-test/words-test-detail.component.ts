import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWordsTest } from 'app/shared/model/words-test.model';

@Component({
    selector: 'jhi-words-test-detail',
    templateUrl: './words-test-detail.component.html'
})
export class WordsTestDetailComponent implements OnInit {
    wordsTest: IWordsTest;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wordsTest }) => {
            this.wordsTest = wordsTest;
        });
    }

    previousState() {
        window.history.back();
    }
}
