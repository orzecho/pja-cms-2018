/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NyanTestModule } from '../../../test.module';
import { ProposedWordDeleteDialogComponent } from 'app/entities/proposed-word/proposed-word-delete-dialog.component';
import { ProposedWordService } from 'app/entities/proposed-word/proposed-word.service';

describe('Component Tests', () => {
    describe('ProposedWord Management Delete Component', () => {
        let comp: ProposedWordDeleteDialogComponent;
        let fixture: ComponentFixture<ProposedWordDeleteDialogComponent>;
        let service: ProposedWordService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [ProposedWordDeleteDialogComponent]
            })
                .overrideTemplate(ProposedWordDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ProposedWordDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ProposedWordService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
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
            ));
        });
    });
});
