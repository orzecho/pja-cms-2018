import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    TrueFalseAnswerComponent,
    TrueFalseAnswerDetailComponent,
    TrueFalseAnswerUpdateComponent,
    TrueFalseAnswerDeletePopupComponent,
    TrueFalseAnswerDeleteDialogComponent,
    trueFalseAnswerRoute,
    trueFalseAnswerPopupRoute
} from './';

const ENTITY_STATES = [...trueFalseAnswerRoute, ...trueFalseAnswerPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TrueFalseAnswerComponent,
        TrueFalseAnswerDetailComponent,
        TrueFalseAnswerUpdateComponent,
        TrueFalseAnswerDeleteDialogComponent,
        TrueFalseAnswerDeletePopupComponent
    ],
    entryComponents: [
        TrueFalseAnswerComponent,
        TrueFalseAnswerUpdateComponent,
        TrueFalseAnswerDeleteDialogComponent,
        TrueFalseAnswerDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanTrueFalseAnswerModule {}
