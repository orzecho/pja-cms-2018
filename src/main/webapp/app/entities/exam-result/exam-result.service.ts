import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IExamResult } from 'app/shared/model/exam-result.model';

type EntityResponseType = HttpResponse<IExamResult>;
type EntityArrayResponseType = HttpResponse<IExamResult[]>;

@Injectable({ providedIn: 'root' })
export class ExamResultService {
    private resourceUrl = SERVER_API_URL + 'api/exam-results';

    constructor(private http: HttpClient) {}

    create(examResult: IExamResult): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(examResult);
        return this.http
            .post<IExamResult>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(examResult: IExamResult): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(examResult);
        return this.http
            .put<IExamResult>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IExamResult>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IExamResult[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(examResult: IExamResult): IExamResult {
        const copy: IExamResult = Object.assign({}, examResult, {
            date: examResult.date != null && examResult.date.isValid() ? examResult.date.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((examResult: IExamResult) => {
            examResult.date = examResult.date != null ? moment(examResult.date) : null;
        });
        return res;
    }
}
