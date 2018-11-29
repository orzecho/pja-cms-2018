import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { TestComponent } from 'app/entities/test/test.component';
import { VocabularyTestComponent } from 'app/entities/test/vocabulary-test.component';

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
    }
];
