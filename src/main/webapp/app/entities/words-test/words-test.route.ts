import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { WordsTest } from 'app/shared/model/words-test.model';
import { WordsTestService } from './words-test.service';
import { WordsTestComponent } from './words-test.component';
import { WordsTestDetailComponent } from './words-test-detail.component';
import { WordsTestUpdateComponent } from './words-test-update.component';
import { WordsTestDeletePopupComponent } from './words-test-delete-dialog.component';
import { IWordsTest } from 'app/shared/model/words-test.model';
import { ShowWrittenTestComponent } from './display/show-written-test.component';

@Injectable({ providedIn: 'root' })
export class WordsTestResolve implements Resolve<IWordsTest> {
    constructor(private service: WordsTestService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((wordsTest: HttpResponse<WordsTest>) => wordsTest.body));
        }
        // const code = route.params['code'] ? route.params['code'] : null;
        // if (code) {
        //     //TODO uwzgłędniać przypadek, w którym testu o podanym kodzie nie istnieje
        //     return this.service.query({
        //         'code.equals': code
        //     }).pipe(map((wordsTest: HttpResponse<WordsTest[]>) => wordsTest.body[0]));
        // }
        return of(new WordsTest());
    }
}

export const wordsTestRoute: Routes = [
    {
        path: 'words-test',
        component: WordsTestComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'WordsTests'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'words-test/:id/view',
        component: WordsTestDetailComponent,
        resolve: {
            wordsTest: WordsTestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WordsTests'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'words-test/new',
        component: WordsTestUpdateComponent,
        resolve: {
            wordsTest: WordsTestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WordsTests'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'words-test/:id/edit',
        component: WordsTestUpdateComponent,
        resolve: {
            wordsTest: WordsTestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WordsTests'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const wordsTestPopupRoute: Routes = [
    {
        path: 'words-test/:id/delete',
        component: WordsTestDeletePopupComponent,
        resolve: {
            wordsTest: WordsTestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WordsTests'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

export const showWrittenTestRoute: Routes = [
    {
        path: 'show-test/written/:code',
        component: ShowWrittenTestComponent,
        resolve: {
            wordsTest: WordsTestResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'WordsTest'
        }
    }
];
