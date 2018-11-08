import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    LessonFileComponent,
    LessonFileDetailComponent,
    LessonFileUpdateComponent,
    LessonFileDeletePopupComponent,
    LessonFileDeleteDialogComponent,
    lessonFileRoute,
    lessonFilePopupRoute
} from './';

const ENTITY_STATES = [...lessonFileRoute, ...lessonFilePopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        LessonFileComponent,
        LessonFileDetailComponent,
        LessonFileUpdateComponent,
        LessonFileDeleteDialogComponent,
        LessonFileDeletePopupComponent
    ],
    entryComponents: [LessonFileComponent, LessonFileUpdateComponent, LessonFileDeleteDialogComponent, LessonFileDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanLessonFileModule {}
