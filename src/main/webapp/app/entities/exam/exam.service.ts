import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IExam } from 'app/shared/model/exam.model';
import { IVocabularyTestItem } from 'app/shared/model/vocabulary-test-item.model';

type EntityResponseType = HttpResponse<IExam>;
type EntityArrayResponseType = HttpResponse<IExam[]>;

@Injectable({ providedIn: 'root' })
export class ExamService {
    private resourceUrl = SERVER_API_URL + 'api/exams';
    private testGenerationUrl = SERVER_API_URL + 'api/generate-test/written';

    constructor(private http: HttpClient) {}

    create(exam: IExam, tags?: string[]): Observable<EntityResponseType> {
        if (tags === undefined) {
            return this.http.post<IExam>(this.resourceUrl, exam, { observe: 'response' });
        }

        return this.http.post<IExam>(this.resourceUrl, exam, {
            observe: 'response',
            params: {
                tags: tags
            }
        });
    }

    update(exam: IExam, tags?: string[]): Observable<EntityResponseType> {
        if (tags === undefined) {
            return this.http.put<IExam>(this.resourceUrl, exam, { observe: 'response' });
        }

        return this.http.put<IExam>(this.resourceUrl, exam, {
            observe: 'response',
            params: {
                tags: tags
            }
        });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IExam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IExam[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    generateWrittenTest(examCode: string): Observable<HttpResponse<IVocabularyTestItem[]>> {
        return this.http.get<IVocabularyTestItem[]>(`${this.testGenerationUrl}/${examCode}`, { observe: 'response' });
    }
}
