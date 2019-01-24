import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExamResult } from 'app/shared/model/exam-result.model';
import { IWrittenAnswer } from 'app/shared/model/written-answer.model';
import { WordService } from 'app/entities/word';
import { IWord } from 'app/shared/model/word.model';

@Component({
    selector: 'jhi-exam-result-detail',
    templateUrl: './exam-result-detail.component.html',
    styleUrls: ['./exam-result-detail.component.css']
})
export class ExamResultDetailComponent implements OnInit {
    examResult: IExamResult;

    constructor(private activatedRoute: ActivatedRoute, private wordService: WordService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ examResult }) => {
            this.examResult = examResult;
        });
    }

    previousState() {
        window.history.back();
    }

    isTranslationSource(writtenAnswer: IWrittenAnswer) {
        return writtenAnswer.translationFrom === 'polish';
    }

    isKanaSource(writtenAnswer: IWrittenAnswer) {
        return writtenAnswer.translationFrom === 'kana';
    }

    isKanjiSource(writtenAnswer: IWrittenAnswer) {
        return writtenAnswer.translationFrom === 'kanji';
    }

    getMaxPoints(examResult: IExamResult): number {
        let maxPoints = 0;
        if (examResult.writtenAnswers) {
            maxPoints = maxPoints + examResult.writtenAnswers.length;
        }
        if (examResult.trueFalseAnswers) {
            maxPoints = maxPoints + examResult.trueFalseAnswers.length;
        }
        return maxPoints;
    }
}
