import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Lesson } from 'app/shared/model/lesson.model';
import { LessonService } from './lesson.service';
import { LessonComponent } from './lesson.component';
import { LessonDetailComponent } from './lesson-detail.component';
import { LessonUpdateComponent } from './lesson-update.component';
import { LessonDeletePopupComponent } from './lesson-delete-dialog.component';
import { ILesson } from 'app/shared/model/lesson.model';

@Injectable({ providedIn: 'root' })
export class LessonResolve implements Resolve<ILesson> {
    constructor(private service: LessonService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((lesson: HttpResponse<Lesson>) => lesson.body));
        }
        return of(new Lesson());
    }
}

export const lessonRoute: Routes = [
    {
        path: 'lesson',
        component: LessonComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Lekcje'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lesson/:id/view',
        component: LessonDetailComponent,
        resolve: {
            lesson: LessonResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Lekcje'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lesson/new',
        component: LessonUpdateComponent,
        resolve: {
            lesson: LessonResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_TEACHER'],
            pageTitle: 'Lekcje'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lesson/:id/edit',
        component: LessonUpdateComponent,
        resolve: {
            lesson: LessonResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_TEACHER'],
            pageTitle: 'Lekcje'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lessonPopupRoute: Routes = [
    {
        path: 'lesson/:id/delete',
        component: LessonDeletePopupComponent,
        resolve: {
            lesson: LessonResolve
        },
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_TEACHER'],
            pageTitle: 'Lekcje'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
