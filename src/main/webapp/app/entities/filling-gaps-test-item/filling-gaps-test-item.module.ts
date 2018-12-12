import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import {
    FillingGapsTestItemComponent,
    FillingGapsTestItemDetailComponent,
    FillingGapsTestItemUpdateComponent,
    FillingGapsTestItemDeletePopupComponent,
    FillingGapsTestItemDeleteDialogComponent,
    fillingGapsTestItemRoute,
    fillingGapsTestItemPopupRoute
} from './';

const ENTITY_STATES = [...fillingGapsTestItemRoute, ...fillingGapsTestItemPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FillingGapsTestItemComponent,
        FillingGapsTestItemDetailComponent,
        FillingGapsTestItemUpdateComponent,
        FillingGapsTestItemDeleteDialogComponent,
        FillingGapsTestItemDeletePopupComponent
    ],
    entryComponents: [
        FillingGapsTestItemComponent,
        FillingGapsTestItemUpdateComponent,
        FillingGapsTestItemDeleteDialogComponent,
        FillingGapsTestItemDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanFillingGapsTestItemModule {}
