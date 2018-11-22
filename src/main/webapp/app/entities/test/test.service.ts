import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IVocabularyTestItem } from 'app/shared/model/vocabulary-test-item.model';

type EntityArrayResponseType = HttpResponse<IVocabularyTestItem[]>;

@Injectable({ providedIn: 'root' })
export class TestService {
    private resourceUrl = SERVER_API_URL + 'api/test';

    constructor(private http: HttpClient) {}

    getVocabularyTest(type: string, tags: string[]): Observable<EntityArrayResponseType> {
        const tagsString = tags.join(',');
        return this.http.get<IVocabularyTestItem[]>(`${this.resourceUrl}/${type}/${tagsString}`, { observe: 'response' });
    }
}
