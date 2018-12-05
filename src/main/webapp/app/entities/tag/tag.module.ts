import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    TagComponent,
    TagDeleteDialogComponent,
    TagDeletePopupComponent,
    TagDetailComponent,
    tagPopupRoute,
    tagRoute,
    TagUpdateComponent
} from './';
import { TableModule } from 'primeng/table';

const ENTITY_STATES = [...tagRoute, ...tagPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES), TableModule],
    declarations: [TagComponent, TagDetailComponent, TagUpdateComponent, TagDeleteDialogComponent, TagDeletePopupComponent],
    entryComponents: [TagComponent, TagUpdateComponent, TagDeleteDialogComponent, TagDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanTagModule {}
