import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { TestComponent } from 'app/entities/test/test.component';
import { VocabularyTestComponent } from 'app/entities/test/vocabulary-test.component';
import { GapsTestComponent } from 'app/entities/test/gaps-test.component';

export const testRoute: Routes = [
    {
        path: 'test',
        component: TestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Testy'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'test/vocabulary/:type/:tags',
        component: VocabularyTestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Testy słownictwa'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'exam/written/:examCode',
        component: VocabularyTestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Examin na słownictwo'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'test/GAPS/:tags',
        component: GapsTestComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Test z wypełnianiem luk'
        },
        canActivate: [UserRouteAccessService]
    }
];
