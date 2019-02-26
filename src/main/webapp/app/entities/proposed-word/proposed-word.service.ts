import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProposedWord, ProposedWord } from 'app/shared/model/proposed-word.model';
import { Word } from 'app/shared/model/word.model';

type EntityResponseType = HttpResponse<IProposedWord>;
type EntityArrayResponseType = HttpResponse<IProposedWord[]>;

@Injectable({ providedIn: 'root' })
export class ProposedWordService {
    private resourceUrl = SERVER_API_URL + 'api/proposed-words';

    constructor(private http: HttpClient) {}

    create(proposedWord: IProposedWord): Observable<EntityResponseType> {
        return this.http.post<IProposedWord>(this.resourceUrl, proposedWord, { observe: 'response' });
    }

    update(proposedWord: IProposedWord): Observable<EntityResponseType> {
        return this.http.put<IProposedWord>(this.resourceUrl, proposedWord, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProposedWord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProposedWord[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    accept(proposedWord: ProposedWord): Observable<HttpResponse<Word>> {
        return this.http.get<Word>(`${this.resourceUrl}/accept/${proposedWord.id}`, { observe: 'response' });
    }
}
