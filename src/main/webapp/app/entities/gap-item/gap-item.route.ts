import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GapItem } from 'app/shared/model/gap-item.model';
import { GapItemService } from './gap-item.service';
import { GapItemComponent } from './gap-item.component';
import { GapItemDetailComponent } from './gap-item-detail.component';
import { GapItemUpdateComponent } from './gap-item-update.component';
import { GapItemDeletePopupComponent } from './gap-item-delete-dialog.component';
import { IGapItem } from 'app/shared/model/gap-item.model';

@Injectable({ providedIn: 'root' })
export class GapItemResolve implements Resolve<IGapItem> {
    constructor(private service: GapItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((gapItem: HttpResponse<GapItem>) => gapItem.body));
        }
        return of(new GapItem());
    }
}

export const gapItemRoute: Routes = [
    {
        path: 'gap-item',
        component: GapItemComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'GapItems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gap-item/:id/view',
        component: GapItemDetailComponent,
        resolve: {
            gapItem: GapItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GapItems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gap-item/new',
        component: GapItemUpdateComponent,
        resolve: {
            gapItem: GapItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GapItems'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'gap-item/:id/edit',
        component: GapItemUpdateComponent,
        resolve: {
            gapItem: GapItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GapItems'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const gapItemPopupRoute: Routes = [
    {
        path: 'gap-item/:id/delete',
        component: GapItemDeletePopupComponent,
        resolve: {
            gapItem: GapItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'GapItems'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
