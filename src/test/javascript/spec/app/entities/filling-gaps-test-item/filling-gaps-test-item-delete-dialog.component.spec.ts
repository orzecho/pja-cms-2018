/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NyanTestModule } from '../../../test.module';
import { FillingGapsTestItemDeleteDialogComponent } from 'app/entities/filling-gaps-test-item/filling-gaps-test-item-delete-dialog.component';
import { FillingGapsTestItemService } from 'app/entities/filling-gaps-test-item/filling-gaps-test-item.service';

describe('Component Tests', () => {
    describe('FillingGapsTestItem Management Delete Component', () => {
        let comp: FillingGapsTestItemDeleteDialogComponent;
        let fixture: ComponentFixture<FillingGapsTestItemDeleteDialogComponent>;
        let service: FillingGapsTestItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [FillingGapsTestItemDeleteDialogComponent]
            })
                .overrideTemplate(FillingGapsTestItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FillingGapsTestItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FillingGapsTestItemService);
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
