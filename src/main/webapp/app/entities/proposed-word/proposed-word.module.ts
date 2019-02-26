import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { NyanAdminModule } from 'app/admin/admin.module';
import {
    ProposedWordComponent,
    ProposedWordDetailComponent,
    ProposedWordUpdateComponent,
    ProposedWordDeletePopupComponent,
    ProposedWordDeleteDialogComponent,
    proposedWordRoute,
    proposedWordPopupRoute
} from './';

const ENTITY_STATES = [...proposedWordRoute, ...proposedWordPopupRoute];

@NgModule({
    imports: [NyanSharedModule, NyanAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProposedWordComponent,
        ProposedWordDetailComponent,
        ProposedWordUpdateComponent,
        ProposedWordDeleteDialogComponent,
        ProposedWordDeletePopupComponent
    ],
    entryComponents: [
        ProposedWordComponent,
        ProposedWordUpdateComponent,
        ProposedWordDeleteDialogComponent,
        ProposedWordDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanProposedWordModule {}
