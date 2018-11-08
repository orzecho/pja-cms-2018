import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { LessonFile } from 'app/shared/model/lesson-file.model';
import { LessonFileService } from './lesson-file.service';
import { LessonFileComponent } from './lesson-file.component';
import { LessonFileDetailComponent } from './lesson-file-detail.component';
import { LessonFileUpdateComponent } from './lesson-file-update.component';
import { LessonFileDeletePopupComponent } from './lesson-file-delete-dialog.component';
import { ILessonFile } from 'app/shared/model/lesson-file.model';

@Injectable({ providedIn: 'root' })
export class LessonFileResolve implements Resolve<ILessonFile> {
    constructor(private service: LessonFileService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((lessonFile: HttpResponse<LessonFile>) => lessonFile.body));
        }
        return of(new LessonFile());
    }
}

export const lessonFileRoute: Routes = [
    {
        path: 'lesson-file',
        component: LessonFileComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'LessonFiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lesson-file/:id/view',
        component: LessonFileDetailComponent,
        resolve: {
            lessonFile: LessonFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LessonFiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lesson-file/new',
        component: LessonFileUpdateComponent,
        resolve: {
            lessonFile: LessonFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LessonFiles'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'lesson-file/:id/edit',
        component: LessonFileUpdateComponent,
        resolve: {
            lessonFile: LessonFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LessonFiles'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const lessonFilePopupRoute: Routes = [
    {
        path: 'lesson-file/:id/delete',
        component: LessonFileDeletePopupComponent,
        resolve: {
            lessonFile: LessonFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'LessonFiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
