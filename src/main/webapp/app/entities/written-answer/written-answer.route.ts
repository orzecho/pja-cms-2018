import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WrittenAnswer } from 'app/shared/model/written-answer.model';
import { WrittenAnswerService } from './written-answer.service';
import { WrittenAnswerComponent } from './written-answer.component';
import { WrittenAnswerDetailComponent } from './written-answer-detail.component';
import { WrittenAnswerUpdateComponent } from './written-answer-update.component';
import { WrittenAnswerDeletePopupComponent } from './written-answer-delete-dialog.component';
import { IWrittenAnswer } from 'app/shared/model/written-answer.model';

@Injectable({ providedIn: 'root' })
export class WrittenAnswerResolve implements Resolve<IWrittenAnswer> {
    constructor(private service: WrittenAnswerService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((writtenAnswer: HttpResponse<WrittenAnswer>) => writtenAnswer.body));
        }
        return of(new WrittenAnswer());
    }
}

export const writtenAnswerRoute: Routes = [
    {
        path: 'written-answer',
        component: WrittenAnswerComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'WrittenAnswers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'written-answer/:id/view',
        component: WrittenAnswerDetailComponent,
        resolve: {
            writtenAnswer: WrittenAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WrittenAnswers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'written-answer/new',
        component: WrittenAnswerUpdateComponent,
        resolve: {
            writtenAnswer: WrittenAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WrittenAnswers'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'written-answer/:id/edit',
        component: WrittenAnswerUpdateComponent,
        resolve: {
            writtenAnswer: WrittenAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WrittenAnswers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const writtenAnswerPopupRoute: Routes = [
    {
        path: 'written-answer/:id/delete',
        component: WrittenAnswerDeletePopupComponent,
        resolve: {
            writtenAnswer: WrittenAnswerResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WrittenAnswers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
