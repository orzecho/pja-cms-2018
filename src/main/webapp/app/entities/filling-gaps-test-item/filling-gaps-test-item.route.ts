import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';
import { FillingGapsTestItemService } from './filling-gaps-test-item.service';
import { FillingGapsTestItemComponent } from './filling-gaps-test-item.component';
import { FillingGapsTestItemDetailComponent } from './filling-gaps-test-item-detail.component';
import { FillingGapsTestItemUpdateComponent } from './filling-gaps-test-item-update.component';
import { FillingGapsTestItemDeletePopupComponent } from './filling-gaps-test-item-delete-dialog.component';
import { IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';

@Injectable({ providedIn: 'root' })
export class FillingGapsTestItemResolve implements Resolve<IFillingGapsTestItem> {
    constructor(private service: FillingGapsTestItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((fillingGapsTestItem: HttpResponse<FillingGapsTestItem>) => fillingGapsTestItem.body));
        }
        return of(new FillingGapsTestItem());
    }
}

export const fillingGapsTestItemRoute: Routes = [
    {
        path: 'filling-gaps-test-item',
        component: FillingGapsTestItemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'FillingGapsTestItems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'filling-gaps-test-item/:id/view',
        component: FillingGapsTestItemDetailComponent,
        resolve: {
            fillingGapsTestItem: FillingGapsTestItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FillingGapsTestItems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'filling-gaps-test-item/new',
        component: FillingGapsTestItemUpdateComponent,
        resolve: {
            fillingGapsTestItem: FillingGapsTestItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FillingGapsTestItems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'filling-gaps-test-item/:id/edit',
        component: FillingGapsTestItemUpdateComponent,
        resolve: {
            fillingGapsTestItem: FillingGapsTestItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FillingGapsTestItems'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const fillingGapsTestItemPopupRoute: Routes = [
    {
        path: 'filling-gaps-test-item/:id/delete',
        component: FillingGapsTestItemDeletePopupComponent,
        resolve: {
            fillingGapsTestItem: FillingGapsTestItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'FillingGapsTestItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
