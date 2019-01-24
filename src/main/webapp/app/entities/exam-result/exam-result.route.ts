import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ExamResult, IExamResult } from 'app/shared/model/exam-result.model';
import { ExamResultService } from './exam-result.service';
import { ExamResultComponent } from './exam-result.component';
import { ExamResultDetailComponent } from './exam-result-detail.component';
import { ExamResultForTeacherComponent } from 'app/entities/exam-result/exam-result-for-teacher.component';

@Injectable({ providedIn: 'root' })
export class ExamResultResolve implements Resolve<IExamResult> {
    constructor(private service: ExamResultService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((examResult: HttpResponse<ExamResult>) => examResult.body));
        }
        return of(new ExamResult());
    }
}

export const examResultRoute: Routes = [
    {
        path: 'exam-result',
        component: ExamResultComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'ExamResults'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'exam-result/:id/view',
        component: ExamResultDetailComponent,
        resolve: {
            examResult: ExamResultResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ROLE_TEACHER'],
            pageTitle: 'ExamResults'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'exam-result-for-teacher',
        component: ExamResultForTeacherComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_TEACHER'],
            defaultSort: 'id,asc',
            pageTitle: 'ExamResults'
        },
        canActivate: [UserRouteAccessService]
    }
];
