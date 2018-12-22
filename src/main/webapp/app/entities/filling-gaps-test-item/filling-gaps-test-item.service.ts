import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';

type EntityResponseType = HttpResponse<IFillingGapsTestItem>;
type EntityArrayResponseType = HttpResponse<IFillingGapsTestItem[]>;

@Injectable({ providedIn: 'root' })
export class FillingGapsTestItemService {
    private resourceUrl = SERVER_API_URL + 'api/filling-gaps-test-items';

    constructor(private http: HttpClient) {}

    create(fillingGapsTestItem: IFillingGapsTestItem): Observable<EntityResponseType> {
        return this.http.post<IFillingGapsTestItem>(this.resourceUrl, fillingGapsTestItem, { observe: 'response' });
    }

    update(fillingGapsTestItem: IFillingGapsTestItem): Observable<EntityResponseType> {
        return this.http.put<IFillingGapsTestItem>(this.resourceUrl, fillingGapsTestItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFillingGapsTestItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFillingGapsTestItem[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
