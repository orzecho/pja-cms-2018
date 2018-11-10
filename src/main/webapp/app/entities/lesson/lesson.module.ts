import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    LessonComponent,
    LessonDetailComponent,
    LessonUpdateComponent,
    LessonDeletePopupComponent,
    LessonDeleteDialogComponent,
    lessonRoute,
    lessonPopupRoute
} from './';
import { ListboxModule } from 'primeng/listbox';
import { MultiSelectModule } from 'primeng/primeng';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const ENTITY_STATES = [...lessonRoute, ...lessonPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES), ListboxModule, MultiSelectModule, BrowserAnimationsModule],
    declarations: [LessonComponent, LessonDetailComponent, LessonUpdateComponent, LessonDeleteDialogComponent, LessonDeletePopupComponent],
    entryComponents: [LessonComponent, LessonUpdateComponent, LessonDeleteDialogComponent, LessonDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanLessonModule {}
