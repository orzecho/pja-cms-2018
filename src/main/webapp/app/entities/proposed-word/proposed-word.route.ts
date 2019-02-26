import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ProposedWord } from 'app/shared/model/proposed-word.model';
import { ProposedWordService } from './proposed-word.service';
import { ProposedWordComponent } from './proposed-word.component';
import { ProposedWordDetailComponent } from './proposed-word-detail.component';
import { ProposedWordUpdateComponent } from './proposed-word-update.component';
import { ProposedWordDeletePopupComponent } from './proposed-word-delete-dialog.component';
import { IProposedWord } from 'app/shared/model/proposed-word.model';

@Injectable({ providedIn: 'root' })
export class ProposedWordResolve implements Resolve<IProposedWord> {
    constructor(private service: ProposedWordService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((proposedWord: HttpResponse<ProposedWord>) => proposedWord.body));
        }
        return of(new ProposedWord());
    }
}

export const proposedWordRoute: Routes = [
    {
        path: 'proposed-word',
        component: ProposedWordComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ProposedWords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'proposed-word/:id/view',
        component: ProposedWordDetailComponent,
        resolve: {
            proposedWord: ProposedWordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProposedWords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'proposed-word/new',
        component: ProposedWordUpdateComponent,
        resolve: {
            proposedWord: ProposedWordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProposedWords'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'proposed-word/:id/edit',
        component: ProposedWordUpdateComponent,
        resolve: {
            proposedWord: ProposedWordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProposedWords'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const proposedWordPopupRoute: Routes = [
    {
        path: 'proposed-word/:id/delete',
        component: ProposedWordDeletePopupComponent,
        resolve: {
            proposedWord: ProposedWordResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ProposedWords'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
