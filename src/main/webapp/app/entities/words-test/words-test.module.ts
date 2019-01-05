import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { NyanAdminModule } from 'app/admin/admin.module';
import { AutoCompleteModule } from 'primeng/primeng';

import { ShowWrittenTestComponent } from './display/show-written-test.component';
import {
    WordsTestComponent,
    WordsTestDetailComponent,
    WordsTestUpdateComponent,
    WordsTestDeletePopupComponent,
    WordsTestDeleteDialogComponent,
    wordsTestRoute,
    wordsTestPopupRoute,
    showWrittenTestRoute
} from './';

const ENTITY_STATES = [...wordsTestRoute, ...wordsTestPopupRoute, ...showWrittenTestRoute];

@NgModule({
    imports: [NyanSharedModule, NyanAdminModule, AutoCompleteModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WordsTestComponent,
        WordsTestDetailComponent,
        WordsTestUpdateComponent,
        WordsTestDeleteDialogComponent,
        WordsTestDeletePopupComponent,
        ShowWrittenTestComponent
    ],
    entryComponents: [WordsTestComponent, WordsTestUpdateComponent, WordsTestDeleteDialogComponent, WordsTestDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanWordsTestModule {}
