import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWordsTest } from 'app/shared/model/words-test.model';
import { IVocabularyTestItem } from 'app/shared/model/vocabulary-test-item.model';
import { IWord } from 'app/shared/model/word.model';

type EntityResponseType = HttpResponse<IWordsTest>;
type EntityArrayResponseType = HttpResponse<IWordsTest[]>;

@Injectable({ providedIn: 'root' })
export class WordsTestService {
    private testModificationUrl = SERVER_API_URL + 'api/words-tests';
    private showingTestUrl = SERVER_API_URL + 'api/show-test';

    constructor(private http: HttpClient) {}

    create(wordsTest: IWordsTest, tags?: string[]): Observable<EntityResponseType> {
        if (tags === undefined) {
            return this.http.post<IWordsTest>(this.testModificationUrl, wordsTest, { observe: 'response' });
        }

        return this.http.post<IWordsTest>(this.testModificationUrl, wordsTest, {
            observe: 'response',
            params: {
                tags: tags
            }
        });
    }

    update(wordsTest: IWordsTest, tags?: string[]): Observable<EntityResponseType> {
        if (tags === undefined) {
            return this.http.put<IWordsTest>(this.testModificationUrl, wordsTest, { observe: 'response' });
        }

        return this.http.put<IWordsTest>(this.testModificationUrl, wordsTest, {
            observe: 'response',
            params: {
                tags: tags
            }
        });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IWordsTest>(`${this.testModificationUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IWordsTest[]>(this.testModificationUrl, { params: options, observe: 'response' });
    }

    generateWrittenTest(testCode: string): Observable<HttpResponse<IVocabularyTestItem[]>> {
        return this.http.get<IVocabularyTestItem[]>(`${this.showingTestUrl}/written/${testCode}`, { observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.testModificationUrl}/${id}`, { observe: 'response' });
    }
}
