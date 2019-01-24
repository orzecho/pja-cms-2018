import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    WrittenAnswerComponent,
    WrittenAnswerDetailComponent,
    WrittenAnswerUpdateComponent,
    WrittenAnswerDeletePopupComponent,
    WrittenAnswerDeleteDialogComponent,
    writtenAnswerRoute,
    writtenAnswerPopupRoute
} from './';

const ENTITY_STATES = [...writtenAnswerRoute, ...writtenAnswerPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        WrittenAnswerComponent,
        WrittenAnswerDetailComponent,
        WrittenAnswerUpdateComponent,
        WrittenAnswerDeleteDialogComponent,
        WrittenAnswerDeletePopupComponent
    ],
    entryComponents: [
        WrittenAnswerComponent,
        WrittenAnswerUpdateComponent,
        WrittenAnswerDeleteDialogComponent,
        WrittenAnswerDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanWrittenAnswerModule {}
