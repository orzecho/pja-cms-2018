import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ITag } from 'app/shared/model/tag.model';
import { Principal } from 'app/core';
import { TestService } from 'app/entities/test/test.service';
import { IVocabularyTestItem, VocabularyTestItem } from 'app/shared/model/vocabulary-test-item.model';

@Component({
    selector: 'jhi-vocabulary-test',
    templateUrl: './vocabulary-test.component.html',
    styleUrls: ['./vocabulary-test.component.css']
})
export class VocabularyTestComponent implements OnInit {
    currentAccount: any;
    tags: string;
    testType: string;
    vocabularyTestItems: VocabularyTestItem[];
    error: any;
    success: any;

    constructor(
        private testService: TestService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute
    ) {
        this.activatedRoute.params.subscribe(params => {
            this.testType = params.type;
            this.tags = params.tags;
            this.loadAll();
        });
    }

    loadAll() {
        this.testService
            .getVocabulary(this.testType, this.tags.split(','))
            .subscribe(
                (res: HttpResponse<IVocabularyTestItem[]>) => (this.vocabularyTestItems = res.body),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    trackId(index: number, item: ITag) {
        return item.id;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    checkKanji(item: VocabularyTestItem) {
        item.kanjiCorrect = item.kanjiFromSystem === item.kanjiFromUser;
    }

    checkKana(item: VocabularyTestItem) {
        item.kanaCorrect = item.kanaFromSystem === item.kanaFromUser;
    }

    checkTranslation(item: VocabularyTestItem) {
        item.translationCorrect = item.translationFromSystem === item.translationFromUser;
    }

    previousState() {
        window.history.back();
    }
}
