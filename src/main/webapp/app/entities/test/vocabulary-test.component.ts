import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ITag } from 'app/shared/model/tag.model';
import { Principal } from 'app/core';
import { TestService } from 'app/entities/test/test.service';
import { IVocabularyTestItem, VocabularyTestItem } from 'app/shared/model/vocabulary-test-item.model';
import { Word } from 'app/shared/model/word.model';
import { IExam } from 'app/shared/model/exam.model';
import { ExamService } from 'app/entities/exam';
import { ExamResultService } from 'app/entities/exam-result';

@Component({
    selector: 'jhi-vocabulary-test',
    templateUrl: './vocabulary-test.component.html',
    styleUrls: ['./vocabulary-test.component.css']
})
export class VocabularyTestComponent implements OnInit {
    currentAccount: any;
    testType: string;
    vocabularyTestItems: VocabularyTestItem[];
    error: any;
    success: any;

    isSaving = false;
    testNotAllowed = false;
    examAlreadyPassed = false;

    // generated by tags
    tags: string;

    // generated by exam
    examCode: string;
    exam: IExam;

    constructor(
        private testService: TestService,
        private examService: ExamService,
        private examResultService: ExamResultService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
        this.activatedRoute.params.subscribe(params => {
            this.testType = params.type;
            this.tags = params.tags;
            this.examCode = params.examCode;
            this.loadAll();
        });
    }

    loadAll() {
        if (this.isExam()) {
            this.examService
                .query({
                    'code.equals': this.examCode
                })
                .subscribe(
                    (res: HttpResponse<IExam[]>) => (this.exam = res.body[0]),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            this.examService.generateWrittenTest(this.examCode).subscribe(
                (res: HttpResponse<IVocabularyTestItem[]>) => {
                    this.vocabularyTestItems = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        } else {
            this.testService
                .getVocabulary(this.testType, this.tags.split(','))
                .subscribe(
                    (res: HttpResponse<IVocabularyTestItem[]>) => (this.vocabularyTestItems = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.examResultService
                .query({
                    'studentId.equals': this.currentAccount.id
                })
                .subscribe(
                    res => {
                        this.examAlreadyPassed = res.body !== undefined && res.body.length !== 0;
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        });
        this.principal.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_TEACHER']).then(isAdminOrTeacher => (this.testNotAllowed = isAdminOrTeacher));
    }

    saveExamResult() {
        this.isSaving = true;
        const examResult = this.testService.converWrittenTestToExamResult(this.vocabularyTestItems, this.exam.id);
        this.examResultService
            .create(examResult)
            .subscribe((res: HttpResponse<IExam>) => this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(exam: IExam) {
        this.isSaving = false;
        this.router.navigate(['/exam-result/', exam.id, 'view']);
    }

    private onSaveError() {
        this.isSaving = false;
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

    getItems(): IVocabularyTestItem[] {
        console.log(this.vocabularyTestItems);
        return this.vocabularyTestItems;
    }

    getJishoLink(word: Word) {
        return 'https://jisho.org/search/'.concat(word.kanji ? word.kanji : word.kana);
    }

    isExam() {
        return this.examCode !== undefined;
    }
}
