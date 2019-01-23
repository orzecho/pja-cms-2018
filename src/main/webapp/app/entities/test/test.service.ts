import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IVocabularyTestItem } from 'app/shared/model/vocabulary-test-item.model';
import { IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';
import { ExamResult, IExamResult } from 'app/shared/model/exam-result.model';
import { WrittenAnswer } from 'app/shared/model/written-answer.model';
import { TestType } from 'app/shared/model/exam.model';

type EntityArrayResponseType = HttpResponse<IVocabularyTestItem[]>;

@Injectable({ providedIn: 'root' })
export class TestService {
    private resourceUrl = SERVER_API_URL + 'api/test';

    constructor(private http: HttpClient) {}

    getVocabulary(type: string, tags: string[]): Observable<EntityArrayResponseType> {
        const tagsString = tags.join(',');
        return this.http.get<IVocabularyTestItem[]>(`${this.resourceUrl}/vocabulary/${type}/${tagsString}`, { observe: 'response' });
    }

    getFillingGaps(tags: string[]): Observable<HttpResponse<IFillingGapsTestItem[]>> {
        const tagsString = tags.join(',');
        return this.http.get<IFillingGapsTestItem[]>(`${this.resourceUrl}/gaps/${tagsString}`, { observe: 'response' });
    }

    converWrittenTestToExamResult(tests: IVocabularyTestItem[], examId: number): IExamResult {
        const examResult = new ExamResult();
        examResult.examId = examId;
        examResult.writtenAnswers = tests.map(this.convertSingleItemToAnswer);
        return examResult;
    }

    private convertSingleItemToAnswer(testItem: IVocabularyTestItem): WrittenAnswer {
        const writtenAnswer = new WrittenAnswer();
        writtenAnswer.translationFrom = testItem.srcTranslationLanguage;
        writtenAnswer.kana = testItem.kanaFromUser;
        writtenAnswer.kanji = testItem.kanjiFromUser;
        writtenAnswer.translation = testItem.translationFromUser;
        writtenAnswer.isRightAnswer = testItem.translationCorrect && testItem.kanaCorrect && testItem.kanjiCorrect;
        writtenAnswer.wordId = testItem.word.id;
        return writtenAnswer;
    }
}
