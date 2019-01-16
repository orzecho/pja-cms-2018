import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ILessonFile } from 'app/shared/model/lesson-file.model';

type EntityResponseType = HttpResponse<ILessonFile>;
type EntityArrayResponseType = HttpResponse<ILessonFile[]>;

@Injectable({ providedIn: 'root' })
export class LessonFileService {
    private resourceUrl = SERVER_API_URL + 'api/lesson-files';

    constructor(private http: HttpClient) {}

    create(lessonFile: ILessonFile): Observable<EntityResponseType> {
        console.log('Czy tu dochodzi: Serwis: ' + lessonFile.name);
        return this.http.post<ILessonFile>(this.resourceUrl, lessonFile, { observe: 'response' });
    }

    update(lessonFile: ILessonFile): Observable<EntityResponseType> {
        return this.http.put<ILessonFile>(this.resourceUrl, lessonFile, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ILessonFile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ILessonFile[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
