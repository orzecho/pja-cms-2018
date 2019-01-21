import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { NyanAdminModule } from 'app/admin/admin.module';
import { AutoCompleteModule } from 'primeng/primeng';
import {
    ExamComponent,
    ExamDeleteDialogComponent,
    ExamDeletePopupComponent,
    ExamDetailComponent,
    examPopupRoute,
    examRoute,
    ExamUpdateComponent
} from './';

const ENTITY_STATES = [...examRoute, ...examPopupRoute];

@NgModule({
    imports: [NyanSharedModule, NyanAdminModule, AutoCompleteModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ExamComponent, ExamDetailComponent, ExamUpdateComponent, ExamDeleteDialogComponent, ExamDeletePopupComponent],
    entryComponents: [ExamComponent, ExamUpdateComponent, ExamDeleteDialogComponent, ExamDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanExamModule {}
