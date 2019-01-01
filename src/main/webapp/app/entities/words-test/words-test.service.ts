import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWordsTest } from 'app/shared/model/words-test.model';

type EntityResponseType = HttpResponse<IWordsTest>;
type EntityArrayResponseType = HttpResponse<IWordsTest[]>;

@Injectable({ providedIn: 'root' })
export class WordsTestService {
    private resourceUrl = SERVER_API_URL + 'api/words-tests';

    constructor(private http: HttpClient) {}

    create(wordsTest: IWordsTest, tags?: string[]): Observable<EntityResponseType> {
        if (tags == undefined) {
            return this.http.post<IWordsTest>(this.resourceUrl, wordsTest, { observe: 'response' });
        }

        return this.http.post<IWordsTest>(this.resourceUrl, wordsTest, {
            observe: 'response',
            params: {
                tags: tags
            }
        });
    }

    update(wordsTest: IWordsTest, tags?: string[]): Observable<EntityResponseType> {
        if (tags == undefined) {
            return this.http.put<IWordsTest>(this.resourceUrl, wordsTest, { observe: 'response' });
        }

        return this.http.put<IWordsTest>(this.resourceUrl, wordsTest, {
            observe: 'response',
            params: {
                tags: tags
            }
        });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IWordsTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IWordsTest[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
