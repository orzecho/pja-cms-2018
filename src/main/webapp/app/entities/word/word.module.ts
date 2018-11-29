import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoCompleteModule } from 'primeng/autocomplete';

import { NyanSharedModule } from 'app/shared';
import {
    WordComponent,
    WordDetailComponent,
    WordUpdateComponent,
    WordDeletePopupComponent,
    WordDeleteDialogComponent,
    wordRoute,
    wordPopupRoute
} from './';

const ENTITY_STATES = [...wordRoute, ...wordPopupRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES), AutoCompleteModule],
    declarations: [WordComponent, WordDetailComponent, WordUpdateComponent, WordDeleteDialogComponent, WordDeletePopupComponent],
    entryComponents: [WordComponent, WordUpdateComponent, WordDeleteDialogComponent, WordDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanWordModule {}
