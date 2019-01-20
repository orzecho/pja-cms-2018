import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { NyanAdminModule } from 'app/admin/admin.module';
import {
    ExamResultComponent,
    ExamResultDetailComponent,
    ExamResultUpdateComponent,
    ExamResultDeletePopupComponent,
    ExamResultDeleteDialogComponent,
    examResultRoute,
    examResultPopupRoute
} from './';

const ENTITY_STATES = [...examResultRoute, ...examResultPopupRoute];

@NgModule({
    imports: [NyanSharedModule, NyanAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ExamResultComponent,
        ExamResultDetailComponent,
        ExamResultUpdateComponent,
        ExamResultDeleteDialogComponent,
        ExamResultDeletePopupComponent
    ],
    entryComponents: [ExamResultComponent, ExamResultUpdateComponent, ExamResultDeleteDialogComponent, ExamResultDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanExamResultModule {}
