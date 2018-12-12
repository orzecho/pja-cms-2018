/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NyanTestModule } from '../../../test.module';
import { GapItemDeleteDialogComponent } from 'app/entities/gap-item/gap-item-delete-dialog.component';
import { GapItemService } from 'app/entities/gap-item/gap-item.service';

describe('Component Tests', () => {
    describe('GapItem Management Delete Component', () => {
        let comp: GapItemDeleteDialogComponent;
        let fixture: ComponentFixture<GapItemDeleteDialogComponent>;
        let service: GapItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [GapItemDeleteDialogComponent]
            })
                .overrideTemplate(GapItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GapItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GapItemService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
