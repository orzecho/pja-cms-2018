import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGapItem } from 'app/shared/model/gap-item.model';

type EntityResponseType = HttpResponse<IGapItem>;
type EntityArrayResponseType = HttpResponse<IGapItem[]>;

@Injectable({ providedIn: 'root' })
export class GapItemService {
    private resourceUrl = SERVER_API_URL + 'api/gap-items';

    constructor(private http: HttpClient) {}

    create(gapItem: IGapItem): Observable<EntityResponseType> {
        return this.http.post<IGapItem>(this.resourceUrl, gapItem, { observe: 'response' });
    }

    update(gapItem: IGapItem): Observable<EntityResponseType> {
        return this.http.put<IGapItem>(this.resourceUrl, gapItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IGapItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IGapItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
