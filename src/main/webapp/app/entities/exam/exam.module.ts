import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { NyanAdminModule } from 'app/admin/admin.module';
import {
    ExamComponent,
    ExamDetailComponent,
    ExamUpdateComponent,
    ExamDeletePopupComponent,
    ExamDeleteDialogComponent,
    examRoute,
    examPopupRoute
} from './';

const ENTITY_STATES = [...examRoute, ...examPopupRoute];

@NgModule({
    imports: [NyanSharedModule, NyanAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ExamComponent, ExamDetailComponent, ExamUpdateComponent, ExamDeleteDialogComponent, ExamDeletePopupComponent],
    entryComponents: [ExamComponent, ExamUpdateComponent, ExamDeleteDialogComponent, ExamDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanExamModule {}
