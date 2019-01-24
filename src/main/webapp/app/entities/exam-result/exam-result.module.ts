import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { NyanAdminModule } from 'app/admin/admin.module';
import { ExamResultComponent, ExamResultDetailComponent, examResultRoute } from './';
import { ExamResultForTeacherComponent } from 'app/entities/exam-result/exam-result-for-teacher.component';

const ENTITY_STATES = [...examResultRoute];

@NgModule({
    imports: [NyanSharedModule, NyanAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [ExamResultComponent, ExamResultDetailComponent, ExamResultForTeacherComponent],
    entryComponents: [ExamResultComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanExamResultModule {}
