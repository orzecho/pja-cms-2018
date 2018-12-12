import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    GapItemComponent,
    GapItemDetailComponent,
    GapItemUpdateComponent,
    GapItemDeletePopupComponent,
    GapItemDeleteDialogComponent,
    gapItemRoute,
    gapItemPopupRoute
} from './';

const ENTITY_STATES = [...gapItemRoute, ...gapItemPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GapItemComponent,
        GapItemDetailComponent,
        GapItemUpdateComponent,
        GapItemDeleteDialogComponent,
        GapItemDeletePopupComponent
    ],
    entryComponents: [GapItemComponent, GapItemUpdateComponent, GapItemDeleteDialogComponent, GapItemDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanGapItemModule {}
