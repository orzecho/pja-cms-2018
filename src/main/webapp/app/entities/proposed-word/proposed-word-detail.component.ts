import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProposedWord } from 'app/shared/model/proposed-word.model';

@Component({
    selector: 'jhi-proposed-word-detail',
    templateUrl: './proposed-word-detail.component.html'
})
export class ProposedWordDetailComponent implements OnInit {
    proposedWord: IProposedWord;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ proposedWord }) => {
            this.proposedWord = proposedWord;
        });
    }

    previousState() {
        window.history.back();
    }
}
