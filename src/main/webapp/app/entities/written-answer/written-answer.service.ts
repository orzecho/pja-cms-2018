import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IWrittenAnswer } from 'app/shared/model/written-answer.model';

type EntityResponseType = HttpResponse<IWrittenAnswer>;
type EntityArrayResponseType = HttpResponse<IWrittenAnswer[]>;

@Injectable({ providedIn: 'root' })
export class WrittenAnswerService {
    private resourceUrl = SERVER_API_URL + 'api/written-answers';

    constructor(private http: HttpClient) {}

    create(writtenAnswer: IWrittenAnswer): Observable<EntityResponseType> {
        return this.http.post<IWrittenAnswer>(this.resourceUrl, writtenAnswer, { observe: 'response' });
    }

    update(writtenAnswer: IWrittenAnswer): Observable<EntityResponseType> {
        return this.http.put<IWrittenAnswer>(this.resourceUrl, writtenAnswer, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IWrittenAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IWrittenAnswer[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
