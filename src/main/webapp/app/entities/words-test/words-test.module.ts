import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { NyanAdminModule } from 'app/admin/admin.module';
import {
    WordsTestComponent,
    WordsTestDetailComponent,
    WordsTestUpdateComponent,
    WordsTestDeletePopupComponent,
    WordsTestDeleteDialogComponent,
    wordsTestRoute,
    wordsTestPopupRoute
} from './';

const ENTITY_STATES = [...wordsTestRoute, ...wordsTestPopupRoute];

@NgModule({
    imports: [NyanSharedModule, NyanAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WordsTestComponent,
        WordsTestDetailComponent,
        WordsTestUpdateComponent,
        WordsTestDeleteDialogComponent,
        WordsTestDeletePopupComponent
    ],
    entryComponents: [WordsTestComponent, WordsTestUpdateComponent, WordsTestDeleteDialogComponent, WordsTestDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanWordsTestModule {}
