import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { TrueFalseAnswer } from 'app/shared/model/true-false-answer.model';
import { TrueFalseAnswerService } from './true-false-answer.service';
import { TrueFalseAnswerComponent } from './true-false-answer.component';
import { TrueFalseAnswerDetailComponent } from './true-false-answer-detail.component';
import { TrueFalseAnswerUpdateComponent } from './true-false-answer-update.component';
import { TrueFalseAnswerDeletePopupComponent } from './true-false-answer-delete-dialog.component';
import { ITrueFalseAnswer } from 'app/shared/model/true-false-answer.model';

@Injectable({ providedIn: 'root' })
export class TrueFalseAnswerResolve implements Resolve<ITrueFalseAnswer> {
    constructor(private service: TrueFalseAnswerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((trueFalseAnswer: HttpResponse<TrueFalseAnswer>) => trueFalseAnswer.body));
        }
        return of(new TrueFalseAnswer());
    }
}

export const trueFalseAnswerRoute: Routes = [
    {
        path: 'true-false-answer',
        component: TrueFalseAnswerComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'TrueFalseAnswers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'true-false-answer/:id/view',
        component: TrueFalseAnswerDetailComponent,
        resolve: {
            trueFalseAnswer: TrueFalseAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TrueFalseAnswers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'true-false-answer/new',
        component: TrueFalseAnswerUpdateComponent,
        resolve: {
            trueFalseAnswer: TrueFalseAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TrueFalseAnswers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'true-false-answer/:id/edit',
        component: TrueFalseAnswerUpdateComponent,
        resolve: {
            trueFalseAnswer: TrueFalseAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TrueFalseAnswers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const trueFalseAnswerPopupRoute: Routes = [
    {
        path: 'true-false-answer/:id/delete',
        component: TrueFalseAnswerDeletePopupComponent,
        resolve: {
            trueFalseAnswer: TrueFalseAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TrueFalseAnswers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
