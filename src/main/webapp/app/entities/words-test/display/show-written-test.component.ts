import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IVocabularyTestItem, VocabularyTestItem } from 'app/shared/model/vocabulary-test-item.model';
import { ITag } from 'app/shared/model/tag.model';
import { Word } from 'app/shared/model/word.model';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { WordsTestService } from 'app/entities/words-test';

@Component({
    selector: 'jhi-show-written-test',
    templateUrl: './show-written-test.component.html',
    styleUrls: ['./show-written-test.component.css']
})
export class ShowWrittenTestComponent implements OnInit {
    currentAccount: any;

    error: any;
    success: any;

    vocabularyTestItems: VocabularyTestItem[];
    testCode: string;

    constructor(
        private wordsTestService: WordsTestService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute
    ) {
        this.activatedRoute.params.subscribe(params => {
            this.testCode = params.code;
            this.loadAll();
        });
    }

    loadAll() {
        this.wordsTestService
            .generateWrittenTest(this.testCode)
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

    getJishoLink(word: Word) {
        return 'https://jisho.org/search/'.concat(word.kanji ? word.kanji : word.kana);
    }
}
