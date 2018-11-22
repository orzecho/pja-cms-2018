import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    TagComponent,
    TagDetailComponent,
    TagUpdateComponent,
    TagDeletePopupComponent,
    TagDeleteDialogComponent,
    tagRoute,
    tagPopupRoute
} from './';
import { TestComponent } from 'app/entities/test/test.component';

const ENTITY_STATES = [...tagRoute, ...tagPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [TestComponent],
    entryComponents: [TestComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanTestModule {}
