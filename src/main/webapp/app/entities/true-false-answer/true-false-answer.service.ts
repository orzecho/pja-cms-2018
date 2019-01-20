import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITrueFalseAnswer } from 'app/shared/model/true-false-answer.model';

type EntityResponseType = HttpResponse<ITrueFalseAnswer>;
type EntityArrayResponseType = HttpResponse<ITrueFalseAnswer[]>;

@Injectable({ providedIn: 'root' })
export class TrueFalseAnswerService {
    private resourceUrl = SERVER_API_URL + 'api/true-false-answers';

    constructor(private http: HttpClient) {}

    create(trueFalseAnswer: ITrueFalseAnswer): Observable<EntityResponseType> {
        return this.http.post<ITrueFalseAnswer>(this.resourceUrl, trueFalseAnswer, { observe: 'response' });
    }

    update(trueFalseAnswer: ITrueFalseAnswer): Observable<EntityResponseType> {
        return this.http.put<ITrueFalseAnswer>(this.resourceUrl, trueFalseAnswer, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITrueFalseAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITrueFalseAnswer[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
