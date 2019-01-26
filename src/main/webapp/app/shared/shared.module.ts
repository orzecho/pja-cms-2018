import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { NyanSharedLibsModule, NyanSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

import { JhMaterialModule } from 'app/shared/jh-material.module';

@NgModule({
    imports: [NyanSharedLibsModule, NyanSharedCommonModule],
    declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    entryComponents: [JhiLoginModalComponent],
    exports: [NyanSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective, JhMaterialModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanSharedModule {}
